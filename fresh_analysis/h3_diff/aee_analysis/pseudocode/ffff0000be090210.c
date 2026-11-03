
undefined4
FUN_ffff0000be090210
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
  FUN_ffff0000be0893c4(8,&local_68);
  FUN_ffff0000be088fc4(1,&local_68);
  FUN_ffff0000be0892f8(param_2,&local_68);
  FUN_ffff0000be088fc4(2,&local_68);
  FUN_ffff0000be0892f8(param_3,&local_68);
  FUN_ffff0000be088fc4(0xffffffffffb8bbaf,&local_68);
  FUN_ffff0000be08924c(DAT_ffff0000be1b7cc0,param_1,&local_68);
  uVar1 = DAT_ffff0000be1b7cc0;
  uVar3 = (ulong)DAT_ffff0000be1b7cc0;
  FUN_ffff0000be088fc4(0xffffffffffb8bbac,&local_68);
  FUN_ffff0000be08924c(DAT_ffff0000be1b7d64,param_1 + uVar3,&local_68);
  uVar1 = DAT_ffff0000be1b7d64 + uVar1;
  FUN_ffff0000be088fc4(0xffffffffffb8bbab,&local_68);
  FUN_ffff0000be08924c(DAT_ffff0000be1b7cc0,param_1 + (ulong)uVar1,&local_68);
  local_6c[0] = *(undefined1 *)(param_1 + (ulong)(DAT_ffff0000be1b7cc0 + uVar1));
  FUN_ffff0000be088fc4(0xffffffffffb8bba9,&local_68);
  FUN_ffff0000be08924c(1,local_6c,&local_68);
  FUN_ffff0000be088fc4(0xffffffffffb8bba8,&local_68);
  FUN_ffff0000be08924c(param_5,param_4,&local_68);
  local_70[0] = 0x20;
  FUN_ffff0000be088fc4(0xffffffffffb8bba7,&local_68);
  FUN_ffff0000be08924c(1,local_70,&local_68);
  if ((local_58 == 0xffffffffffffffff) || (local_60 < local_58)) {
    uVar2 = 0x10d002;
    FUN_ffff0000be05fd98
              (s__s_DICE_Payload_CwtEncode_failed_ffff0000be0ad137,s__DICE__ffff0000be0bd9dd,
               0x10d002);
  }
  else {
    uVar2 = 0;
    *param_8 = local_58;
  }
  return uVar2;
}


