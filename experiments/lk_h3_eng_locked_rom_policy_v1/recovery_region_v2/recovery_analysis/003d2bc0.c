
/* Device::GetReason() const */

void Device::GetReason(void)

{
  long lVar1;
  long in_x0;
  undefined8 *in_x8;
  byte *pbVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  pbVar2 = *(byte **)(in_x0 + 0x10);
  if (pbVar2 == (byte *)0x0) {
    *(undefined1 *)in_x8 = 0;
    *(undefined1 *)(in_x8 + 3) = 0;
  }
  else {
    if ((*pbVar2 & 1) == 0) {
      local_30 = *(undefined8 *)(pbVar2 + 0x10);
      uStack_38 = *(undefined8 *)(pbVar2 + 8);
      local_40 = *(undefined8 *)pbVar2;
    }
    else {
      std::string::__init_copy_ctor_external
                ((string *)&local_40,*(char **)(pbVar2 + 0x10),*(ulong *)(pbVar2 + 8));
    }
    in_x8[1] = uStack_38;
    *in_x8 = local_40;
    in_x8[2] = local_30;
    *(undefined1 *)(in_x8 + 3) = 1;
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
