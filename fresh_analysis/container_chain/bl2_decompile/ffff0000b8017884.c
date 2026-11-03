
void FUN_ffff0000b8017884(uint param_1)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  
  if ((int)param_1 < 0) {
    FUN_ffff0000b8017884(0);
    FUN_ffff0000b8017884(1);
    FUN_ffff0000b8017884(2);
    param_1 = 3;
  }
  lVar1 = (ulong)param_1 * 0x48;
  if (3 < param_1) {
    pcVar3 = s__GZ_INIT__MBLOCK___s__failed_to_g_ffff0000b808eac4;
LAB_ffff0000b8017900:
    FUN_ffff0000b804c33c(pcVar3,s_gz_mblock_free_ffff0000b8091725,param_1);
    return;
  }
  if (*(long *)(&DAT_ffff0000b80e8bd8 + lVar1) != 0) {
    iVar2 = FUN_ffff0000b800d264();
    if (iVar2 != 0) {
      pcVar3 = s__GZ_INIT__MBLOCK___s__failed_to_f_ffff0000b809f576;
      goto LAB_ffff0000b8017900;
    }
    *(long *)(&DAT_ffff0000b80e8bd8 + lVar1) = 0;
    if ((&DAT_ffff0000b80e8c08)[lVar1] != '\0') {
      *(undefined8 *)(&DAT_ffff0000b80e8be0 + lVar1) = 0;
      *(undefined4 *)(&DAT_ffff0000b80e8bf8 + lVar1) = 0;
    }
  }
  return;
}


