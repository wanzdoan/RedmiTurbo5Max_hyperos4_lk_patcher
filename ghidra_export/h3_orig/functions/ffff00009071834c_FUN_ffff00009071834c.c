// ffff00009071834c  size=60  FUN_ffff00009071834c

void FUN_ffff00009071834c(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  FUN_ffff00009071832c(&DAT_ffff0000907fe9c0);
  *(undefined4 *)(lVar2 + 0x28) = 0;
  *(uint *)(lVar2 + 0x34) = *(uint *)(lVar2 + 0x34) | 0x10;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  FUN_ffff0000907179f0();
  FUN_ffff000090718388();
  do {
    FUN_ffff000090708020();
  } while( true );
}


