
undefined4 FUN_ffff0000907b62d8(int param_1,undefined8 param_2,ulong *param_3)

{
  undefined8 uVar1;
  undefined4 uVar2;
  undefined8 local_38;
  ulong local_30;
  ulong local_28;
  
  local_30 = 0x10;
  uVar1 = 0xffffffffffffffdd;
  if (param_1 != 0x30) {
    uVar1 = 0xfffffffffffffff9;
  }
  local_28 = 0;
  local_38 = param_2;
  FUN_ffff0000907af1d0(1,&local_38);
  FUN_ffff0000907aedd0(1,&local_38);
  FUN_ffff0000907aedd0(uVar1,&local_38);
  if ((local_28 == 0xffffffffffffffff) || (local_30 < local_28)) {
    uVar2 = 0x10d002;
    FUN_ffff00009077e11c
              (s__s_DICE_Cert_ProtectAttrEncode_f_ffff0000907d22bb,s__DICE__ffff0000907f55ec,
               0x10d002);
  }
  else {
    uVar2 = 0;
    *param_3 = local_28;
  }
  return uVar2;
}


