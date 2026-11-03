
undefined8 FUN_ffff00009070e30c(undefined8 param_1)

{
  undefined *puVar1;
  undefined *puVar2;
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
  
  uVar3 = FUN_ffff00009077de20(param_1,0,0x10);
  puVar2 = PTR_DAT_ffff0000908398d0;
  uVar5 = *(uint *)PTR_DAT_ffff0000908398c8;
  *(undefined4 *)PTR_DAT_ffff0000908398d0 = 0;
  if (uVar5 < uVar3) {
    pcVar7 = s_data_is_too_large_ffff0000907f0d4d;
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
    FUN_ffff00009077f6fc(&local_70,0x40,s_DATA_08x_ffff0000907f5c7a,uVar3);
    uVar6 = FUN_ffff00009077f250(&local_70);
    iVar4 = FUN_ffff00009070fa54(&local_70,uVar6);
    if (iVar4 < 0) {
      return 0;
    }
    FUN_ffff00009077e11c(s_Resp___s_ffff0000907d25b1,&local_70);
    puVar1 = PTR_DAT_ffff0000908398c0;
    FUN_ffff00009077e11c
              (s_download_base__0x_x__length__0x__ffff0000907f7b6e,
               *(undefined4 *)PTR_DAT_ffff0000908398c0,uVar3);
    uVar5 = FUN_ffff00009070f968(*(undefined8 *)puVar1,uVar3);
    if ((-1 < (int)uVar5) && (uVar5 == uVar3)) {
      *(uint *)puVar2 = uVar3;
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      return 1;
    }
    pcVar7 = s_Read_USB_error_ffff0000907e7461;
  }
  FUN_ffff000090709c9c(pcVar7);
  return 0;
}


