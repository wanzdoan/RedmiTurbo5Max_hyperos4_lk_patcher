// ffff000090736534  size=316  FUN_ffff000090736534

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090736534(void)

{
  char *pcVar1;
  long lVar2;
  ushort uVar3;
  undefined8 local_68;
  undefined2 local_60;
  char local_58 [8];
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined2 local_28;
  short local_8 [2];
  ushort local_4 [2];
  
  local_28 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_48 = 0;
  uStack_40 = 0;
  local_4[0] = 0;
  local_8[0] = 0;
  lVar2 = _DAT_ffff00009086db50[2];
  local_60 = 0;
  local_58 = (char  [8])s_PMIC_UVLO__ffff0000908048c4._0_8_;
  uStack_50 = 0x3d4f;
  local_68 = 0;
  if (*(short *)(lVar2 + 0x3c) != 0) {
    (*(code *)_DAT_ffff00009086db50[1])
              (*(short *)(lVar2 + 0x3c),0x4729,*(undefined2 *)(lVar2 + 0x3e),
               *(undefined2 *)(lVar2 + 0x40));
    lVar2 = _DAT_ffff00009086db50[2];
  }
  if (*(short *)(lVar2 + 0x4e) != 0) {
    (*(code *)*_DAT_ffff00009086db50)
              (*(short *)(lVar2 + 0x4e),local_4,*(undefined2 *)(lVar2 + 0x50),
               *(undefined2 *)(lVar2 + 0x52));
    lVar2 = _DAT_ffff00009086db50[2];
  }
  if (*(short *)(lVar2 + 0x54) == 0) {
    uVar3 = 0;
  }
  else {
    (*(code *)*_DAT_ffff00009086db50)
              (*(short *)(lVar2 + 0x54),local_8,*(undefined2 *)(lVar2 + 0x56),
               *(undefined2 *)(lVar2 + 0x58));
    uVar3 = local_8[0] << 8;
    lVar2 = _DAT_ffff00009086db50[2];
  }
  uVar3 = uVar3 | local_4[0];
  if (*(short *)(lVar2 + 0x3c) != 0) {
    (*(code *)_DAT_ffff00009086db50[1])
              (*(short *)(lVar2 + 0x3c),0,*(undefined2 *)(lVar2 + 0x3e),
               *(undefined2 *)(lVar2 + 0x40));
  }
  FUN_ffff00009077f38c(&local_68,&DAT_ffff0000907fe827,uVar3);
  if (uVar3 == 0) {
    pcVar1 = s_PMIC_UVLO_NoUVLO_ffff0000907eac32;
  }
  else {
    pcVar1 = (char *)FUN_ffff00009077ed80(local_58,&local_68);
  }
  FUN_ffff000090718f34(pcVar1);
  return;
}


