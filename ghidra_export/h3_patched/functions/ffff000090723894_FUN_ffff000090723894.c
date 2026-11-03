// ffff000090723894  size=256  FUN_ffff000090723894

void FUN_ffff000090723894(code *param_1)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  lVar1 = FUN_ffff00009077d24c(0x100000);
  if (lVar1 == 0) {
    FUN_ffff00009077de54
              (s_dramc__malloc_failed_at__s_ffff0000907dc44e,
               s_save_calibration_data_ffff0000907ddba2);
    return;
  }
  lVar2 = FUN_ffff00009077bd60(s_dram_para_ffff0000907ddbb8);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s_dramc__Partition___s__is_not_exi_ffff0000907ddbc2,s_dram_para_ffff0000907ddbb8);
    goto LAB_ffff00009072397c;
  }
  FUN_ffff00009077eb88(lVar1,0,0x100000);
  pcVar3 = (char *)FUN_ffff00009077bdf4(lVar2,lVar1,0,0x100000);
  if ((ulong)pcVar3 >> 0x14 == 0) {
    pcVar4 = s_dramc__WARN___read_size_from_dra_ffff0000907cf414;
LAB_ffff000090723970:
    FUN_ffff00009077de54(pcVar4,pcVar3);
  }
  else {
    uVar5 = FUN_ffff00009077b06c(lVar1);
    uVar6 = (*param_1)(uVar5,pcVar3);
    if ((uVar6 & 1) == 0) {
      pcVar4 = s__s__failed_to_set_calibration_da_ffff0000907cf441;
      pcVar3 = s_save_calibration_data_ffff0000907ddba2;
      goto LAB_ffff000090723970;
    }
  }
  FUN_ffff00009077bce4(lVar2);
LAB_ffff00009072397c:
  thunk_FUN_ffff0000907c2ad8(lVar1);
  return;
}


