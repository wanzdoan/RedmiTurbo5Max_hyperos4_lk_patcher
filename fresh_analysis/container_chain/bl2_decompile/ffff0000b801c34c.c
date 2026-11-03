
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b801c34c(undefined8 *param_1,int param_2)

{
  uint uVar1;
  undefined8 uVar2;
  undefined8 local_48;
  undefined *puStack_40;
  undefined8 local_38;
  
  if (DAT_ffff0000b811b388 == (undefined *)0x0) {
    _DAT_ffff0000b811b380 = DAT_ffff0000b80e9060;
    DAT_ffff0000b811b388 = PTR_PTR_ffff0000b80e9068;
    DAT_ffff0000b811b390 = DAT_ffff0000b80e9070;
  }
  local_48 = _DAT_ffff0000b811b380;
  puStack_40 = DAT_ffff0000b811b388;
  local_38 = DAT_ffff0000b811b390;
  uVar1 = FUN_ffff0000b801c270(&local_48,*param_1);
  if ((-1 < (int)uVar1) && ((int)uVar1 < DAT_ffff0000b811b380)) {
    if (((*(int *)(DAT_ffff0000b811b388 + (ulong)uVar1 * 0x20 + 0x1c) == 1) && (param_2 != 0)) &&
       (*(int *)(param_1 + 2) == 0)) {
      FUN_ffff0000b8077590(DAT_ffff0000b811b390,1,1);
    }
    FUN_ffff0000b804c33c(s_brightness_set_by_disp_pwm___d_ffff0000b808b802,param_2);
    uVar2 = FUN_ffff0000b803c814(param_2);
    return uVar2;
  }
  FUN_ffff0000b804c33c(s_get_the_LED_index_by_name_failed_ffff0000b808a945);
  return 0xffffffff;
}


