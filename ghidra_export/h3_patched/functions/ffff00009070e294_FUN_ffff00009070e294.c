// ffff00009070e294  size=280  FUN_ffff00009070e294

undefined8 FUN_ffff00009070e294(undefined8 param_1)

{
  undefined8 *puVar1;
  uint *puVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  undefined8 uVar6;
  char *pcVar7;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  uVar3 = FUN_ffff00009077db58(param_1,0,0x10);
  puVar2 = DAT_ffff0000908398b0;
  uVar5 = *DAT_ffff0000908398a8;
  *DAT_ffff0000908398b0 = 0;
  if (uVar5 < uVar3) {
    pcVar7 = s_data_is_too_large_ffff0000907f0bc5;
  }
  else {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    FUN_ffff00009077f434(&local_70,0x40,s_DATA_08x_ffff0000907f5aec,uVar3);
    uVar6 = FUN_ffff00009077ef88(&local_70);
    iVar4 = FUN_ffff00009070f9dc(&local_70,uVar6);
    if (iVar4 < 0) {
      return 0;
    }
    FUN_ffff00009077de54(s_Resp___s_ffff0000907d2574,&local_70);
    puVar1 = DAT_ffff0000908398a0;
    FUN_ffff00009077de54
              (s_download_base__0x_x__length__0x__ffff0000907f79e4,
               *(undefined4 *)DAT_ffff0000908398a0,uVar3);
    uVar5 = FUN_ffff00009070f8f0(*puVar1,uVar3);
    if ((-1 < (int)uVar5) && (uVar5 == uVar3)) {
      *puVar2 = uVar3;
      FUN_ffff000090709d50(&DAT_ffff0000907df27e);
      return 1;
    }
    pcVar7 = s_Read_USB_error_ffff0000907e7306;
  }
  FUN_ffff000090709d10(pcVar7);
  return 0;
}


