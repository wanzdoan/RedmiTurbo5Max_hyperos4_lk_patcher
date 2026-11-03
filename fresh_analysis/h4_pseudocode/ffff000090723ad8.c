
void FUN_ffff000090723ad8(code *param_1)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  lVar1 = FUN_ffff00009077d514(0x100000);
  if (lVar1 == 0) {
    FUN_ffff00009077e11c
              (s_dramc__malloc_failed_at__s_ffff0000907dc53b,
               s_save_calibration_data_ffff0000907ddc5c);
    return;
  }
  lVar2 = FUN_ffff00009077c028(s_dram_para_ffff0000907ddc72);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s_dramc__Partition___s__is_not_exi_ffff0000907ddc7c,s_dram_para_ffff0000907ddc72);
    goto LAB_ffff000090723bc0;
  }
  FUN_ffff00009077ee50(lVar1,0,0x100000);
  pcVar3 = (char *)FUN_ffff00009077c0bc(lVar2,lVar1,0,0x100000);
  if ((ulong)pcVar3 >> 0x14 == 0) {
    pcVar4 = s_dramc__WARN___read_size_from_dra_ffff0000907cf433;
LAB_ffff000090723bb4:
    FUN_ffff00009077e11c(pcVar4,pcVar3);
  }
  else {
    uVar5 = FUN_ffff00009077b334(lVar1);
    uVar6 = (*param_1)(uVar5,pcVar3);
    if ((uVar6 & 1) == 0) {
      pcVar4 = s__s__failed_to_set_calibration_da_ffff0000907cf460;
      pcVar3 = s_save_calibration_data_ffff0000907ddc5c;
      goto LAB_ffff000090723bb4;
    }
  }
  FUN_ffff00009077bfac(lVar2);
LAB_ffff000090723bc0:
  thunk_FUN_ffff0000907c314c(lVar1);
  return;
}


