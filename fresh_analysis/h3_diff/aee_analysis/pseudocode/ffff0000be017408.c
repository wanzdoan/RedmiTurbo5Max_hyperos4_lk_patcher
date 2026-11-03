
undefined8 FUN_ffff0000be017408(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  long lVar4;
  long local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  uVar2 = FUN_ffff0000be05fa9c(param_1,0,0x10);
  local_70 = 0;
  uStack_68 = 0;
  FUN_ffff0000be0611dc(&local_60,0x40,s_DATA_08llx_ffff0000be0c1912,uVar2);
  uVar3 = FUN_ffff0000be060e5c(&local_60);
  iVar1 = FUN_ffff0000be018ca4(&local_60,uVar3);
  if (-1 < iVar1) {
    FUN_ffff0000be060ae0(&local_70,0,0x10);
    lVar4 = FUN_ffff0000be05df6c(&DAT_ffff0000be107b44);
    if ((lVar4 == 0) && (lVar4 = FUN_ffff0000be05de5c(&DAT_ffff0000be107b44), lVar4 == 0)) {
      FUN_ffff0000be0137e8(s_partition_does_not_exist_ffff0000be0c3d06);
    }
    else {
      uStack_68 = *(undefined8 *)(lVar4 + 0x20);
      local_70 = lVar4;
      FUN_ffff0000be05fd98
                (s_download_part_name__s__part_max__ffff0000be0ae569,&DAT_ffff0000be107b44,uStack_68
                 ,uVar2);
      iVar1 = FUN_ffff0000be0189f0(uVar2,&local_70);
      if (-1 < iVar1) {
        FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
        FUN_ffff0000be05def0(lVar4);
        return 1;
      }
      FUN_ffff0000be0611dc(&local_60,0x40,s_Transmission_FAIL____d__ffff0000be0c3d1f,iVar1);
      FUN_ffff0000be0137e8(&local_60);
      FUN_ffff0000be05def0(lVar4);
    }
  }
  return 0;
}


