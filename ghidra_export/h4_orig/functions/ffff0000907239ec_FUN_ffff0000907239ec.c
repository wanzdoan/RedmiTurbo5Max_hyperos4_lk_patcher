// ffff0000907239ec  size=4  FUN_ffff0000907239ec

void FUN_ffff0000907239ec(void)

{
  char *pcVar1;
  int iVar2;
  long lVar3;
  undefined4 uVar4;
  
  lVar3 = FUN_ffff000090721d30(s_ddr_reserved_stress_enable_ffff0000907fb538);
  if ((((lVar3 != 0) && (iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907df35d), iVar2 == 0)) &&
      (iVar2 = FUN_ffff000090781fb0(), iVar2 != 0)) && (iVar2 = FUN_ffff000090781fc4(), iVar2 != 0))
  {
    iVar2 = FUN_ffff000090724950();
    uVar4 = 0xa4;
    pcVar1 = s__s__d__ddr_reserved_stress__DRAM_ffff0000907d5d5b;
    if (-1 < iVar2) {
      uVar4 = 0xa6;
      pcVar1 = s__s__d__ddr_reserved_stress__DRAM_ffff0000907e912f;
    }
    FUN_ffff00009077e11c(pcVar1,s_ddr_reserved_stress_hook_ffff0000907d5d87,uVar4);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907227e0(1,1);
  }
  return;
}


