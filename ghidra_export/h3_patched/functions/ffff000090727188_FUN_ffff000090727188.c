// ffff000090727188  size=188  FUN_ffff000090727188

void FUN_ffff000090727188(uint param_1)

{
  char cVar1;
  long lVar2;
  int iVar3;
  char *pcVar4;
  
  if ((int)param_1 < 0) {
    FUN_ffff000090727188(0);
    FUN_ffff000090727188(1);
    FUN_ffff000090727188(2);
    param_1 = 3;
  }
  lVar2 = (ulong)param_1 * 0x48;
  if (3 < param_1) {
    pcVar4 = s__GZ_INIT__MBLOCK___s__failed_to_g_ffff0000907e0ec8;
LAB_ffff000090727204:
    FUN_ffff00009077de54(pcVar4,s_gz_mblock_free_ffff0000907e5e98,param_1);
    return;
  }
  if (*(long *)(&DAT_ffff000090818c88 + lVar2) != 0) {
    iVar3 = FUN_ffff00009071a6ac();
    if (iVar3 != 0) {
      pcVar4 = s__GZ_INIT__MBLOCK___s__failed_to_f_ffff0000907fec99;
      goto LAB_ffff000090727204;
    }
    cVar1 = *(char *)(lVar2 + -0xffff6f7e7348);
    *(long *)(&DAT_ffff000090818c88 + lVar2) = 0;
    if (cVar1 != '\0') {
      *(undefined8 *)(lVar2 + -0xffff6f7e7370) = 0;
      *(undefined4 *)(lVar2 + -0xffff6f7e7358) = 0;
    }
  }
  return;
}


