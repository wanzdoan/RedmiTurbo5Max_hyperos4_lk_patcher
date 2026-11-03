// ffff00009070e140  size=316  FUN_ffff00009070e140

undefined8 FUN_ffff00009070e140(undefined8 param_1)

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
  uVar2 = FUN_ffff00009077db58(param_1,0,0x10);
  local_70 = 0;
  uStack_68 = 0;
  FUN_ffff00009077f434(&local_60,0x40,s_DATA_08llx_ffff0000907faf3e,uVar2);
  uVar3 = FUN_ffff00009077ef88(&local_60);
  iVar1 = FUN_ffff00009070f9dc(&local_60,uVar3);
  if (-1 < iVar1) {
    FUN_ffff00009077eb88(&local_70,0,0x10);
    lVar4 = FUN_ffff00009077bd60(0xffff00009083d39c);
    if ((lVar4 == 0) && (lVar4 = FUN_ffff00009077bc50(0xffff00009083d39c), lVar4 == 0)) {
      FUN_ffff000090709d10(s_partition_does_not_exist_ffff0000907fe857);
    }
    else {
      uStack_68 = *(undefined8 *)(lVar4 + 0x20);
      local_70 = lVar4;
      FUN_ffff00009077de54
                (s_download_part_name__s__part_max__ffff0000907df2d8,0xffff00009083d39c,uStack_68,
                 uVar2);
      iVar1 = FUN_ffff00009070f728(uVar2,&local_70);
      if (-1 < iVar1) {
        FUN_ffff000090709d50(&DAT_ffff0000907df27e);
        FUN_ffff00009077bce4(lVar4);
        return 1;
      }
      FUN_ffff00009077f434(&local_60,0x40,s_Transmission_FAIL____d__ffff0000907fe870,iVar1);
      FUN_ffff000090709d10(&local_60);
      FUN_ffff00009077bce4(lVar4);
    }
  }
  return 0;
}


