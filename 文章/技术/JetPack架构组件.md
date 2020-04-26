# Lifecycle

    Lifecycle 通过 模板方法模式 和 观察者模式，将生命周期管理的复杂操作进行封装。

    被观察者                    观察者
    Lifecycle           LifecycleEventObserver
    生命周期状态         生命周期方法（跟随状态改变）

    LifecycleOwner
    包含 LifecycleRegistry 与 ObserverWithState 并进行了封装处理。

## 相关的实现类

1、Lifecycle
    // 添加|删除观察者
    addObserver(@NonNull LifecycleObserver observer);
    void removeObserver(@NonNull LifecycleObserver observer);
    // 根据状态发布事件
    State getCurrentState();
    enum State  例如：CREATED
    enum Event  例如：ON_CREATE

    实现类：LifecycleRegistry
        private 方法，改变状态并发布状态改变的事件：moveToState(State next);
        public 方法，提供 handleLifecycleEvent(@NonNull Lifecycle.Event event) 供 LifecycleOwner 调用；就是在其内部调用 moveToState(State next);

2、LifecycleOwner

    拥有 Lifecycle
    LifecycleOwner 本身拥有生命周期回调函数，在回调函数处，改变 Lifecycle State 状态，并发布状态改变事件。
    通常是在基类就已经处理好了。（模板方法模式）

    例如 FragmentActivity 在其生命周期回调函数中就调用了 LifecycleRegistry handleLifecycleEvent() 方法。

3、LifecycleEventObserver
    // 跟随 LifecycleOwner 发出的 Lifecycle.Event 来作出对应的回调
    onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event);

   实现类：FullLifecycleObserverAdapter
    实现了 onStateChanged() 方法，调用 FullLifecycleObserver 的生命周期回调方法

4、FullLifecycleObserver

    包含与 Activity 一样的所有生命周期方法，在 FullLifecycleObserverAdapter 中已经写好了回调的时机。（这就是模板方法模式）

## 处理流程梳理

    LifecycleOwner

    lifecycleOwner.getLifecycle().addObserver(lifecycleObserver);

    在 addObserver() 方法中，做了两件事，
        第一是 lifecycleOwner 缓存了 lifecycleObserver；
        第二是 while 循环 dispatchEvent() 分发生命周期事件，在这儿调用了 lifecycleObserver.onStateChanged(owner, event);


总结：LifecycleOwner 拥有 Lifecycle，并在自身生命周期变化时，改变 Lifecycle 的 State，并发布事件；该 Lifecycle 的观察者就能接收到状态变化的事件，并执行状态改变的回调方法。


# ViewModel

    ViewModel's only responsibility is to manage the data for the UI.
    It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.

    ViewModel 并不是由用户直接通过构造器生成的，而是通过 ViewModelProvider 来获取;

## 相关的实现类

1、ViewModelStoreOwner

    // if an owner of this {@code ViewModelStore} is destroyed and recreated due to configuration changes,
    // new instance of an owner should still have the same old instance of {@code ViewModelStore}
    ViewModelStore getViewModelStore(); // 如果因为 Activity 的 configuration 改变，那么持有的还是之前的 ViewModelStore

    getLifecycle().addObserver(new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source,
                @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (!isChangingConfigurations()) {
                    getViewModelStore().clear();
                }
            }
        }
    });

2、ViewModelStore

    If an owner of this {@code ViewModelStore} is destroyed and is not going to be recreated,
    then it should call {@link #clear()} on this {@code ViewModelStore},
    so {@code ViewModels} would be notified that they are no longer used.

    // 通过 HashMap 存储 ViewModel
    private final HashMap<String, ViewModel> mMap = new HashMap<>();

    final void put(String key, ViewModel viewModel) {
            ViewModel oldViewModel = mMap.put(key, viewModel);
            if (oldViewModel != null) {
                oldViewModel.onCleared();
            }
        }

    public final void clear() {
            for (ViewModel vm : mMap.values()) {
                vm.clear();
            }
            mMap.clear();
        }

3、ViewModelProvider

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} via the given
     * {@code Factory} and retain them in a store of the given {@code ViewModelStoreOwner}.
     *
     * @param owner   a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
     *                retain {@code ViewModels} 为 ViewModelStoreOwner 存储 ViewModel，并用于数据交互
     * @param factory a {@code Factory} which will be used to instantiate
     *                new {@code ViewModels} 创建并初始化 ViewModel
     */
    public ViewModelProvider(@NonNull ViewModelStoreOwner owner, @NonNull Factory factory) {
            this(owner.getViewModelStore(), factory);
        }

    ViewModelProvider.Factory

    // 如果 own 的 mViewModelStore 有该 viewmodel 则直接取，不然第一次就创建 viewmodel 并存储
    public <T extends ViewModel> T get(@NonNull String key, @NonNull Class<T> modelClass) {
            ViewModel viewModel = mViewModelStore.get(key);

            if (modelClass.isInstance(viewModel)) {
                if (mFactory instanceof OnRequeryFactory) {
                    ((OnRequeryFactory) mFactory).onRequery(viewModel);
                }
                return (T) viewModel;
            }

            if (mFactory instanceof KeyedFactory) {
                viewModel = ((KeyedFactory) (mFactory)).create(key, modelClass);
            } else {
                viewModel = (mFactory).create(modelClass);
            }
            mViewModelStore.put(key, viewModel);
            return (T) viewModel;
        }

4、ViewModel

    // 为 Activity|Fragment 管理数据
    ViewModel is a class that is responsible for preparing and managing the data for
    an {@link android.app.Activity Activity} or a {@link androidx.fragment.app.Fragment Fragment}.

    // 观察的数据变化 ViewModel 通常是根据 LiveData 与 DataBinding 来实现的。
   The purpose of the ViewModel is to acquire and keep the information that is necessary for an
   Activity or a Fragment. The Activity or the Fragment should be able to observe changes in the ViewModel.
   ViewModels usually expose this information via {@link LiveData} or Android Data Binding.
   You can also use any observability construct from you favorite framework.

   // 不应该持有 Activity|Fragment 的引用
    ViewModel's only responsibility is to manage the data for the UI. It <b>should never</b> access
    your view hierarchy or hold a reference back to the Activity or the Fragment.

    ViewModel的寿命和View、LifecycleOwners实例无关。
    这样做也方便了您编写覆盖ViewModel的测试，因为它不用去考虑生命周期。
    ViewModel对象可以包含LifecycleObservers，例如LiveData对象。 但是，ViewModel对象绝不能去观察有生命周期感知能力的Observer（如LiveData对象）。
    通常在系统第一次调用Activity对象的onCreate()方法时初始化ViewModel。


# LiveData

    LiveData 通过观察者模式，将源数据变化通知给还在 active state 内的观察者。

        被观察者                    观察者
        LiveData<T>                LifecycleBoundObserver 包装了 LifecycleOwner 与 Observer

1、LiveData

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        assertMainThread("observe");
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
        ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        owner.getLifecycle().addObserver(wrapper);
    }

    看最后一行代码，就知道 LiveData 的观察者 isActive 就是依赖 LifecycleOwner 的生命周期。并且在 destroy 时，自动解绑 observer。


    setValue(T t) 在主线程执行
    postValue(T t) post 至主线程执行

    @MainThread
    protected void setValue(T value) {
        assertMainThread("setValue");
        mVersion++;
        mData = value;
        dispatchingValue(null);
    }

    如果数据变化，则调用 setValue() 则可进行事件分发给观察者,
    执行 ObserverWrapper observer.mObserver.onChanged((T) mData);



# DataBinding

    Android 实现 MVVM 中数据与视图双向绑定的最佳实践方法。

    MVVM:
        V 层持有 VM 层引用，
        VM 层不持有 V 层引用，只持有 M 层引用，与 M 打交道，M 数据变化了，通过 DataBinding 双向绑定到视图中。

    DataBinding 双向绑定也是通过观察者模式实现的。








