
undefined8 FUN_ffff0000be01755c(undefined8 param_1)

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
  
  uVar3 = FUN_ffff0000be05fa9c(param_1,0,0x10);
  puVar2 = PTR_DAT_ffff0000be0f55a0;
  uVar5 = *(uint *)PTR_DAT_ffff0000be0f5598;
  *(undefined4 *)PTR_DAT_ffff0000be0f55a0 = 0;
  if (uVar5 < uVar3) {
    pcVar7 = s_data_is_too_large_ffff0000be0bac06;
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
    FUN_ffff0000be0611dc(&local_70,0x40,s_DATA_08x_ffff0000be0be077,uVar3);
    uVar6 = FUN_ffff0000be060e5c(&local_70);
    iVar4 = FUN_ffff0000be018ca4(&local_70,uVar6);
    if (iVar4 < 0) {
      return 0;
    }
    FUN_ffff0000be05fd98(s_Resp___s_ffff0000be0a583e,&local_70);
    puVar1 = PTR_DAT_ffff0000be0f5590;
    FUN_ffff0000be05fd98
              (s_download_base__0x_x__length__0x__ffff0000be0bf4ea,
               *(undefined4 *)PTR_DAT_ffff0000be0f5590,uVar3);
    uVar5 = FUN_ffff0000be018bb8(*(undefined8 *)puVar1,uVar3);
    if ((-1 < (int)uVar5) && (uVar5 == uVar3)) {
      *(uint *)puVar2 = uVar3;
      FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
      return 1;
    }
    pcVar7 = s_Read_USB_error_ffff0000be0b3eae;
  }
  FUN_ffff0000be0137e8(pcVar7);
  return 0;
}


