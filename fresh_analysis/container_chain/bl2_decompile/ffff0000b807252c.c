
undefined4
FUN_ffff0000b807252c
          (long param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,undefined8 param_5,
          undefined8 param_6,ulong param_7,ulong *param_8)

{
  uint uVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined1 local_70 [4];
  undefined1 local_6c [4];
  undefined8 local_68;
  ulong local_60;
  ulong local_58;
  
  local_58 = 0;
  local_68 = param_6;
  local_60 = param_7;
  FUN_ffff0000b806b9c8(8,&local_68);
  FUN_ffff0000b806b5c8(1,&local_68);
  FUN_ffff0000b806b8fc(param_2,&local_68);
  FUN_ffff0000b806b5c8(2,&local_68);
  FUN_ffff0000b806b8fc(param_3,&local_68);
  FUN_ffff0000b806b5c8(0xffffffffffb8bbaf,&local_68);
  FUN_ffff0000b806b850(DAT_ffff0000b81b9a00,param_1,&local_68);
  uVar1 = DAT_ffff0000b81b9a00;
  uVar3 = (ulong)DAT_ffff0000b81b9a00;
  FUN_ffff0000b806b5c8(0xffffffffffb8bbac,&local_68);
  FUN_ffff0000b806b850(DAT_ffff0000b81b9aa4,param_1 + uVar3,&local_68);
  uVar1 = DAT_ffff0000b81b9aa4 + uVar1;
  FUN_ffff0000b806b5c8(0xffffffffffb8bbab,&local_68);
  FUN_ffff0000b806b850(DAT_ffff0000b81b9a00,param_1 + (ulong)uVar1,&local_68);
  local_6c[0] = *(undefined1 *)(param_1 + (ulong)(DAT_ffff0000b81b9a00 + uVar1));
  FUN_ffff0000b806b5c8(0xffffffffffb8bba9,&local_68);
  FUN_ffff0000b806b850(1,local_6c,&local_68);
  FUN_ffff0000b806b5c8(0xffffffffffb8bba8,&local_68);
  FUN_ffff0000b806b850(param_5,param_4,&local_68);
  local_70[0] = 0x20;
  FUN_ffff0000b806b5c8(0xffffffffffb8bba7,&local_68);
  FUN_ffff0000b806b850(1,local_70,&local_68);
  if ((local_58 == 0xffffffffffffffff) || (local_60 < local_58)) {
    uVar2 = 0x10d002;
    FUN_ffff0000b804c33c
              (s__s_DICE_Payload_CwtEncode_failed_ffff0000b808cca3,s__DICE__ffff0000b8099de7,
               0x10d002);
  }
  else {
    uVar2 = 0;
    *param_8 = local_58;
  }
  return uVar2;
}


