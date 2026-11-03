
void FUN_ffff0000be020f74(code *param_1)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  lVar1 = FUN_ffff0000be05f1bc(0x100000);
  if (lVar1 == 0) {
    FUN_ffff0000be05fd98
              (s_dramc__malloc_failed_at__s_ffff0000be0ac70d,
               s_save_calibration_data_ffff0000be0ad5e1);
    return;
  }
  lVar2 = FUN_ffff0000be05df6c(s_dram_para_ffff0000be0ad5f7);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s_dramc__Partition___s__is_not_exi_ffff0000be0ad601,s_dram_para_ffff0000be0ad5f7);
    goto LAB_ffff0000be02105c;
  }
  FUN_ffff0000be060ae0(lVar1,0,0x100000);
  pcVar3 = (char *)FUN_ffff0000be05e000(lVar2,lVar1,0,0x100000);
  if ((ulong)pcVar3 >> 0x14 == 0) {
    pcVar4 = s_dramc__WARN___read_size_from_dra_ffff0000be0a333a;
LAB_ffff0000be021050:
    FUN_ffff0000be05fd98(pcVar4,pcVar3);
  }
  else {
    uVar5 = FUN_ffff0000be05d228(lVar1);
    uVar6 = (*param_1)(uVar5,pcVar3);
    if ((uVar6 & 1) == 0) {
      pcVar4 = s__s__failed_to_set_calibration_da_ffff0000be0a3367;
      pcVar3 = s_save_calibration_data_ffff0000be0ad5e1;
      goto LAB_ffff0000be021050;
    }
  }
  FUN_ffff0000be05def0(lVar2);
LAB_ffff0000be02105c:
  thunk_FUN_ffff0000be09937c(lVar1);
  return;
}


