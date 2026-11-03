
void FUN_ffff0000be01b450(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  FUN_ffff0000be01b430(&DAT_ffff0000be0c3dfd);
  *(undefined4 *)(lVar2 + 0x28) = 0;
  *(uint *)(lVar2 + 0x34) = *(uint *)(lVar2 + 0x34) | 0x10;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  FUN_ffff0000be01ab2c();
  FUN_ffff0000be01b48c();
  do {
    FUN_ffff0000be007658();
  } while( true );
}


