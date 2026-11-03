// ffff0000907237ac  size=132  FUN_ffff0000907237ac

void FUN_ffff0000907237ac(void)

{
  char *pcVar1;
  int iVar2;
  long lVar3;
  undefined4 uVar4;
  
  lVar3 = FUN_ffff000090721af0(s_ddr_reserved_stress_enable_ffff0000907fb39c);
  if ((((lVar3 != 0) && (iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df2a3), iVar2 == 0)) &&
      (iVar2 = FUN_ffff000090781ce8(), iVar2 != 0)) && (iVar2 = FUN_ffff000090781cfc(), iVar2 != 0))
  {
    iVar2 = FUN_ffff00009072470c();
    uVar4 = 0xa4;
    pcVar1 = s__s__d__ddr_reserved_stress__DRAM_ffff0000907d5cc5;
    if (-1 < iVar2) {
      uVar4 = 0xa6;
      pcVar1 = s__s__d__ddr_reserved_stress__DRAM_ffff0000907e8fd4;
    }
    FUN_ffff00009077de54(pcVar1,s_ddr_reserved_stress_hook_ffff0000907d5cf1,uVar4);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009072259c(1,1);
  }
  return;
}


