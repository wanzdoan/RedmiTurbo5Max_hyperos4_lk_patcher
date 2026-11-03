// ffff000090718490  size=60  FUN_ffff000090718490

void FUN_ffff000090718490(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  FUN_ffff000090718470(&DAT_ffff0000907feb68);
  *(undefined4 *)(lVar2 + 0x28) = 0;
  *(uint *)(lVar2 + 0x34) = *(uint *)(lVar2 + 0x34) | 0x10;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  FUN_ffff000090717b34();
  FUN_ffff0000907184cc();
  do {
    FUN_ffff000090707fac();
  } while( true );
}


