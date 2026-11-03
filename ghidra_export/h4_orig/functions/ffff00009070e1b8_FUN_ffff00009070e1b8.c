// ffff00009070e1b8  size=316  FUN_ffff00009070e1b8

undefined8 FUN_ffff00009070e1b8(undefined8 param_1)

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
  uVar2 = FUN_ffff00009077de20(param_1,0,0x10);
  local_70 = 0;
  uStack_68 = 0;
  FUN_ffff00009077f6fc(&local_60,0x40,s_DATA_08llx_ffff0000907fb0da,uVar2);
  uVar3 = FUN_ffff00009077f250(&local_60);
  iVar1 = FUN_ffff00009070fa54(&local_60,uVar3);
  if (-1 < iVar1) {
    FUN_ffff00009077ee50(&local_70,0,0x10);
    lVar4 = FUN_ffff00009077c028(0xffff00009083d3d8);
    if ((lVar4 == 0) && (lVar4 = FUN_ffff00009077bf18(0xffff00009083d3d8), lVar4 == 0)) {
      FUN_ffff000090709c9c(s_partition_does_not_exist_ffff0000907fe9ff);
    }
    else {
      uStack_68 = *(undefined8 *)(lVar4 + 0x20);
      local_70 = lVar4;
      FUN_ffff00009077e11c
                (s_download_part_name__s__part_max__ffff0000907df392,0xffff00009083d3d8,uStack_68,
                 uVar2);
      iVar1 = FUN_ffff00009070f7a0(uVar2,&local_70);
      if (-1 < iVar1) {
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        FUN_ffff00009077bfac(lVar4);
        return 1;
      }
      FUN_ffff00009077f6fc(&local_60,0x40,s_Transmission_FAIL____d__ffff0000907fea18,iVar1);
      FUN_ffff000090709c9c(&local_60);
      FUN_ffff00009077bfac(lVar4);
    }
  }
  return 0;
}


