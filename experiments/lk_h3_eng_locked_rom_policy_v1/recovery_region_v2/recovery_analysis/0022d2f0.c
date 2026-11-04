
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* IsRoDebuggable() */

uint IsRoDebuggable(void)

{
  long lVar1;
  uint uVar2;
  bool in_w2;
  base local_40;
  char local_3f [5];
  char acStack_3a [3];
  char acStack_37 [5];
  undefined1 local_32;
  void *local_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  local_40 = (base)0x1a;
  local_3f = (char  [5])s_ro_debuggable_00143021._0_5_;
  acStack_3a = (char  [3])s_ro_debuggable_00143021._5_3_;
  acStack_37 = (char  [5])s_ro_debuggable_00143021._8_5_;
  local_32 = 0;
  uVar2 = android::base::GetBoolProperty(&local_40,(string *)0x0,in_w2);
  if (((byte)local_40 & 1) != 0) {
    operator_delete(local_30);
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return uVar2 & 1;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
