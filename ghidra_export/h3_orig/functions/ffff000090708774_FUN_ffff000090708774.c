// ffff000090708774  size=44  FUN_ffff000090708774

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090708774(void)

{
  ulong uVar1;
  long lVar2;
  
  lVar2 = tpidr_el1;
  _DAT_ffff000090843678 = 0;
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  (**(code **)(lVar2 + 0x278))(*(undefined8 *)(lVar2 + 0x280));
                    /* WARNING: Subroutine does not return */
  FUN_ffff000090717ec0();
}


