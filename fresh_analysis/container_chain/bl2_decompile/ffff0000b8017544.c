
bool FUN_ffff0000b8017544(void)

{
  bool bVar1;
  byte *pbVar2;
  
  pbVar2 = (byte *)FUN_ffff0000b8016c54();
  bVar1 = false;
  if (pbVar2 != (byte *)0x0) {
    bVar1 = (*pbVar2 & 1) == 0;
  }
  return bVar1;
}


