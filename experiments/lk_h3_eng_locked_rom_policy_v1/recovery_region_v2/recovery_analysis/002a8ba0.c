
void property_list(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  undefined8 local_38;
  undefined8 uStack_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  local_38 = param_1;
  uStack_30 = param_2;
  __system_property_foreach(property_list_callback,&local_38);
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
