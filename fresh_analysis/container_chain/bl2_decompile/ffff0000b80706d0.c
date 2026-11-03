
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff0000b80706d0
          (undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4,int param_5,
          undefined4 *param_6,int param_7)

{
  undefined4 uVar1;
  undefined8 uVar2;
  char *pcVar3;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  ulong uStack_58;
  
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  if (param_5 == 0) {
    pcVar3 = s_CDI_Attest_ffff0000b8086ca3;
  }
  else {
    if (param_5 != 1) {
      return 0x10d003;
    }
    pcVar3 = s_CDI_Seal_ffff0000b809a50a;
  }
  FUN_ffff0000b8069ad4(&local_60,pcVar3,0x10);
  uStack_58 = uStack_58 & 0xffffffffffffff;
  uVar1 = FUN_ffff0000b8069a54(&local_60);
  uVar2 = FUN_ffff0000b80615f4(param_1,param_2,&local_a0,DAT_ffff0000b81b99fc);
  if ((int)uVar2 != 0) {
    return uVar2;
  }
  uVar2 = FUN_ffff0000b805b4e8
                    (&local_a0,DAT_ffff0000b81b9a00,&local_60,uVar1,param_3,param_4,param_6,0x20);
  if ((int)uVar2 != 0) {
    return uVar2;
  }
  if (param_7 - 1U < 2) {
    if (param_5 == 0) {
      _DAT_ffff00001800d700 = *param_6;
      _DAT_ffff00001800d704 = param_6[1];
      _DAT_ffff00001800d708 = param_6[2];
      _DAT_ffff00001800d70c = param_6[3];
      _DAT_ffff00001800d710 = param_6[4];
      _DAT_ffff00001800d714 = param_6[5];
      _DAT_ffff00001800d718 = param_6[6];
      _DAT_ffff00001800d71c = param_6[7];
      return 0;
    }
    _DAT_ffff00001800d724 = param_6[1];
    _DAT_ffff00001800d728 = param_6[2];
    _DAT_ffff00001800d72c = param_6[3];
    _DAT_ffff00001800d730 = param_6[4];
    _DAT_ffff00001800d734 = param_6[5];
    _DAT_ffff00001800d738 = param_6[6];
    _DAT_ffff00001800d73c = param_6[7];
    _DAT_ffff00001800d720 = *param_6;
  }
  else {
    if ((param_7 != 3) && (param_7 != 0)) {
      return 0x10d011;
    }
    if (DAT_ffff0000b8107700 == 0) {
      return 0x10d00f;
    }
    if (param_5 == 0) {
      FUN_ffff0000b8069a2c(DAT_ffff0000b8107700 + (ulong)DAT_ffff0000b81b9a74 + 1,param_6,0x20);
      return 0;
    }
    FUN_ffff0000b8069a2c(DAT_ffff0000b8107700 + (ulong)DAT_ffff0000b81b9a74 + 0x21,param_6,0x20);
  }
  uVar2 = FUN_ffff0000b80703d0
                    (&DAT_ffff0000b81b9a04,param_6,&DAT_ffff0000b81b9a24,param_3,
                     &DAT_ffff0000b81b9a44,&local_a0,DAT_ffff0000b81b9a00);
  if ((int)uVar2 == 0) {
    return 0;
  }
  return uVar2;
}


