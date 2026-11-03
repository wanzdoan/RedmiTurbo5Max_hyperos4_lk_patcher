
void FUN_ffff0000b800bab0(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  FUN_ffff0000b800ba90(&DAT_ffff0000b809f2de);
  *(undefined4 *)(lVar2 + 0x28) = 0;
  *(uint *)(lVar2 + 0x34) = *(uint *)(lVar2 + 0x34) | 0x10;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  FUN_ffff0000b800b18c();
  FUN_ffff0000b800baec();
  do {
    FUN_ffff0000b80078e8();
  } while( true );
}


