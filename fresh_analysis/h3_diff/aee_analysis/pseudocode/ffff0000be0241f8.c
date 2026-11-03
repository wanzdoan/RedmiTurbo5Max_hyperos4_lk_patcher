
undefined * FUN_ffff0000be0241f8(undefined8 param_1)

{
  uint uVar1;
  undefined *puVar2;
  undefined8 local_38;
  undefined *puStack_30;
  undefined8 local_28;
  
  if (DAT_ffff0000be117ba0 == (undefined *)0x0) {
    DAT_ffff0000be117b98 = DAT_ffff0000be0d65c0;
    DAT_ffff0000be117ba0 = PTR_PTR_ffff0000be0d65c8;
    DAT_ffff0000be117ba8 = DAT_ffff0000be0d65d0;
  }
  local_38 = DAT_ffff0000be117b98;
  puStack_30 = DAT_ffff0000be117ba0;
  local_28 = DAT_ffff0000be117ba8;
  uVar1 = FUN_ffff0000be02429c(&local_38,param_1);
  if (((int)uVar1 < 0) || ((int)DAT_ffff0000be117b98 <= (int)uVar1)) {
    FUN_ffff0000be05fd98(s_get_the_LED_index_by_name_failed_ffff0000be0aa351);
    puVar2 = (undefined *)0x0;
  }
  else {
    puVar2 = DAT_ffff0000be117ba0 + (ulong)uVar1 * 0x20 + 8;
  }
  return puVar2;
}


