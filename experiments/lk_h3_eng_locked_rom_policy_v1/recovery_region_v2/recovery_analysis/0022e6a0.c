
/* std::__function::__func<PromptAndWait(Device*, InstallResult)::$_0,
   std::allocator<PromptAndWait(Device*, InstallResult)::$_0>, bool ()>::__clone() const [clone
   .85e66e54385d1cc64007633fa39bd65a] */

void __thiscall
std::__function::
__func<PromptAndWait(Device*,InstallResult)::$_0,std::allocator<PromptAndWait(Device*,InstallResult)::$_0>,bool()>
::__clone(__func<PromptAndWait(Device*,InstallResult)::__0,std::allocator<PromptAndWait(Device*,InstallResult)::__0>,bool()>
          *this)

{
  undefined8 *puVar1;
  undefined8 uVar2;

  puVar1 = operator_new(0x10);
  uVar2 = *(undefined8 *)(this + 8);
  *puVar1 = &PTR_SetBootMonitor_00424138;
  puVar1[1] = uVar2;
  return;
}
