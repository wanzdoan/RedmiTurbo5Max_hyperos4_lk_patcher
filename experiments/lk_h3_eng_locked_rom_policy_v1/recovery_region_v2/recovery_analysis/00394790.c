
/* ensure_path_mounted(std::string const&) */

int ensure_path_mounted(string *param_1)

{
  long lVar1;
  uint uVar2;
  ushort local_40 [8];
  void *local_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  local_40[0] = 0;
  uVar2 = android::fs_mgr::EnsurePathMounted((vector *)&DAT_0057bcf0,param_1,(string *)local_40);
  if ((local_40[0] & 1) != 0) {
    operator_delete(local_30);
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return (uVar2 & 1) - 1;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
