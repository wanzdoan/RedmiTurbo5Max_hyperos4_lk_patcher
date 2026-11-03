
void FUN_ffff0000be007da8(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  DAT_ffff0000be10c6e8 = 0;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  (**(code **)(lVar2 + 0x278))(*(undefined8 *)(lVar2 + 0x280));
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be01afc4();
}


