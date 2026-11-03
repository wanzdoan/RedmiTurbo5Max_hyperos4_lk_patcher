
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined * FUN_ffff0000b801c1cc(undefined8 param_1)

{
  uint uVar1;
  undefined *puVar2;
  undefined8 local_38;
  undefined *puStack_30;
  undefined8 local_28;
  
  if (DAT_ffff0000b811b388 == (undefined *)0x0) {
    _DAT_ffff0000b811b380 = DAT_ffff0000b80e9060;
    DAT_ffff0000b811b388 = PTR_PTR_ffff0000b80e9068;
    DAT_ffff0000b811b390 = DAT_ffff0000b80e9070;
  }
  local_38 = _DAT_ffff0000b811b380;
  puStack_30 = DAT_ffff0000b811b388;
  local_28 = DAT_ffff0000b811b390;
  uVar1 = FUN_ffff0000b801c270(&local_38,param_1);
  if (((int)uVar1 < 0) || (DAT_ffff0000b811b380 <= (int)uVar1)) {
    FUN_ffff0000b804c33c(s_get_the_LED_index_by_name_failed_ffff0000b808a945);
    puVar2 = (undefined *)0x0;
  }
  else {
    puVar2 = DAT_ffff0000b811b388 + (ulong)uVar1 * 0x20 + 8;
  }
  return puVar2;
}


