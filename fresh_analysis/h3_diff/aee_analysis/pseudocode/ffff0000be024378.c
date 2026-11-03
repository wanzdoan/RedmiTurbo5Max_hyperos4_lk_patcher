
undefined8 FUN_ffff0000be024378(undefined8 *param_1,int param_2)

{
  uint uVar1;
  undefined8 uVar2;
  undefined8 local_48;
  undefined *puStack_40;
  undefined8 local_38;
  
  if (DAT_ffff0000be117ba0 == (undefined *)0x0) {
    DAT_ffff0000be117b98 = DAT_ffff0000be0d65c0;
    DAT_ffff0000be117ba0 = PTR_PTR_ffff0000be0d65c8;
    DAT_ffff0000be117ba8 = DAT_ffff0000be0d65d0;
  }
  local_48 = DAT_ffff0000be117b98;
  puStack_40 = DAT_ffff0000be117ba0;
  local_38 = DAT_ffff0000be117ba8;
  uVar1 = FUN_ffff0000be02429c(&local_48,*param_1);
  if ((-1 < (int)uVar1) && ((int)uVar1 < (int)DAT_ffff0000be117b98)) {
    if (((*(int *)(DAT_ffff0000be117ba0 + (ulong)uVar1 * 0x20 + 0x1c) == 1) && (param_2 != 0)) &&
       (*(int *)(param_1 + 2) == 0)) {
      FUN_ffff0000be0961ec(DAT_ffff0000be117ba8,1,1);
    }
    FUN_ffff0000be05fd98(s_brightness_set_by_disp_pwm___d_ffff0000be0ab574,param_2);
    uVar2 = FUN_ffff0000be045bd8(param_2);
    return uVar2;
  }
  FUN_ffff0000be05fd98(s_get_the_LED_index_by_name_failed_ffff0000be0aa351);
  return 0xffffffff;
}


