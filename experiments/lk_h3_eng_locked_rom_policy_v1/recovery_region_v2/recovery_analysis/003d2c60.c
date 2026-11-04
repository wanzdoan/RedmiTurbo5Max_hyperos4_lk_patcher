
/* Device::GetStage() const */

void Device::GetStage(void)

{
  long lVar1;
  long in_x0;
  undefined8 *in_x8;
  long lVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  lVar2 = *(long *)(in_x0 + 0x10);
  if (lVar2 == 0) {
    *(undefined1 *)in_x8 = 0;
    *(undefined1 *)(in_x8 + 3) = 0;
  }
  else {
    if ((*(byte *)(lVar2 + 0x18) & 1) == 0) {
      uStack_38 = *(undefined8 *)(lVar2 + 0x20);
      local_40 = *(undefined8 *)(lVar2 + 0x18);
      local_30 = *(undefined8 *)(lVar2 + 0x28);
    }
    else {
      std::string::__init_copy_ctor_external
                ((string *)&local_40,*(char **)(lVar2 + 0x28),*(ulong *)(lVar2 + 0x20));
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
