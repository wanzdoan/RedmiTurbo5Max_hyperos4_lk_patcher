// ffff000090718204  size=60  FUN_ffff000090718204

bool FUN_ffff000090718204(void)

{
  int iVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  if ((*(byte *)(lVar2 + 0x34) & 0x18) != 0) {
    return false;
  }
  iVar1 = *(int *)(lVar2 + 0x30);
  *(int *)(lVar2 + 0x30) = iVar1 + -1;
  return iVar1 < 2;
}


