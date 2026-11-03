
void FUN_ffff0000b8015894(code *param_1)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  lVar1 = FUN_ffff0000b804bccc(0x100000);
  if (lVar1 == 0) {
    FUN_ffff0000b804c33c
              (s_dramc__malloc_failed_at__s_ffff0000b808c503,
               s_save_calibration_data_ffff0000b808cede);
    return;
  }
  lVar2 = FUN_ffff0000b804a99c(s_dram_para_ffff0000b808cef4);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c
              (s_dramc__Partition___s__is_not_exi_ffff0000b808cefe,s_dram_para_ffff0000b808cef4);
    goto LAB_ffff0000b801597c;
  }
  FUN_ffff0000b804d070(lVar1,0,0x100000);
  pcVar3 = (char *)FUN_ffff0000b804aa30(lVar2,lVar1,0,0x100000);
  if ((ulong)pcVar3 >> 0x14 == 0) {
    pcVar4 = s_dramc__WARN___read_size_from_dra_ffff0000b808517e;
LAB_ffff0000b8015970:
    FUN_ffff0000b804c33c(pcVar4,pcVar3);
  }
  else {
    uVar5 = FUN_ffff0000b8049ec8(lVar1);
    uVar6 = (*param_1)(uVar5,pcVar3);
    if ((uVar6 & 1) == 0) {
      pcVar4 = s__s__failed_to_set_calibration_da_ffff0000b80851ab;
      pcVar3 = s_save_calibration_data_ffff0000b808cede;
      goto LAB_ffff0000b8015970;
    }
  }
  FUN_ffff0000b804a920(lVar2);
LAB_ffff0000b801597c:
  thunk_FUN_ffff0000b8082558(lVar1);
  return;
}


