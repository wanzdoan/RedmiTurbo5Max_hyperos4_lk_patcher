// ffff0000907b3c44  size=568  FUN_ffff0000907b3c44

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff0000907b3c44
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
    pcVar3 = s_CDI_Attest_ffff0000907d2273;
  }
  else {
    if (param_5 != 1) {
      return 0x10d003;
    }
    pcVar3 = s_CDI_Seal_ffff0000907f58a4;
  }
  FUN_ffff0000907ad01c(&local_60,pcVar3,0x10);
  uStack_58 = uStack_58 & 0xffffffffffffff;
  uVar1 = FUN_ffff0000907acf9c(&local_60);
  uVar2 = FUN_ffff0000907a262c(param_1,param_2,&local_a0,_DAT_ffff00009090d73c);
  if ((int)uVar2 != 0) {
    return uVar2;
  }
  uVar2 = FUN_ffff000090798d50
                    (&local_a0,_DAT_ffff00009090d740,&local_60,uVar1,param_3,param_4,param_6,0x20);
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
    if (DAT_ffff000090837eb8 == 0) {
      return 0x10d00f;
    }
    if (param_5 == 0) {
      FUN_ffff0000907acf74(DAT_ffff000090837eb8 + (ulong)_DAT_ffff00009090d7b4 + 1,param_6,0x20);
      return 0;
    }
    FUN_ffff0000907acf74(DAT_ffff000090837eb8 + (ulong)_DAT_ffff00009090d7b4 + 0x21,param_6,0x20);
  }
  uVar2 = FUN_ffff0000907b3944
                    (0xffff00009090d744,param_6,0xffff00009090d764,param_3,0xffff00009090d784,
                     &local_a0,_DAT_ffff00009090d740);
  if ((int)uVar2 == 0) {
    return 0;
  }
  return uVar2;
}


