
int FUN_ffff0000b803ea94(void)

{
  int iVar1;
  long lVar2;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  lVar2 = FUN_ffff0000b803dec4(DAT_ffff0000b81298b0);
  if ((lVar2 == 0) || (iVar1 = *(int *)(lVar2 + 0x6b0), iVar1 == 0)) {
    iVar1 = 0x7ff;
  }
  return iVar1;
}


