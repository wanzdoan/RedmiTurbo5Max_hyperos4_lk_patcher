
void FUN_ffff000090730b40(void)

{
  uint uVar1;
  int iVar2;
  
  if ((((DAT_ffff00009086da30 != 0) && (0 < DAT_ffff00009086da4c)) &&
      (-1 < (int)DAT_ffff00009086da48)) && ((int)DAT_ffff00009086da48 < DAT_ffff00009086da4c)) {
    iVar2 = 1;
    uVar1 = DAT_ffff00009086da48;
    while( true ) {
      FUN_ffff00009073138c(DAT_ffff00009086da30 + (ulong)uVar1 * 0x20);
      if (DAT_ffff00009086da4c <= iVar2) break;
      uVar1 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar1 * 0x20 + 0x18);
      if ((int)uVar1 < 0) {
        return;
      }
      iVar2 = iVar2 + 1;
      if (DAT_ffff00009086da4c <= (int)uVar1) {
        return;
      }
    }
  }
  return;
}


