
void FUN_ffff0000907273cc(uint param_1)

{
  char cVar1;
  long lVar2;
  int iVar3;
  char *pcVar4;
  
  if ((int)param_1 < 0) {
    FUN_ffff0000907273cc(0);
    FUN_ffff0000907273cc(1);
    FUN_ffff0000907273cc(2);
    param_1 = 3;
  }
  lVar2 = (ulong)param_1 * 0x48;
  if (3 < param_1) {
    pcVar4 = s__GZ_INIT__MBLOCK___s__failed_to_g_ffff0000907e0fb0;
LAB_ffff000090727448:
    FUN_ffff00009077e11c(pcVar4,s_gz_mblock_free_ffff0000907e5fc3,param_1);
    return;
  }
  if (*(long *)(&DAT_ffff000090818ca8 + lVar2) != 0) {
    iVar3 = FUN_ffff00009071a7f0();
    if (iVar3 != 0) {
      pcVar4 = s__GZ_INIT__MBLOCK___s__failed_to_f_ffff0000907fee41;
      goto LAB_ffff000090727448;
    }
    cVar1 = *(char *)(lVar2 + -0xffff6f7e7328);
    *(long *)(&DAT_ffff000090818ca8 + lVar2) = 0;
    if (cVar1 != '\0') {
      *(undefined8 *)(lVar2 + -0xffff6f7e7350) = 0;
      *(undefined4 *)(lVar2 + -0xffff6f7e7338) = 0;
    }
  }
  return;
}


