// ffff000090721014  size=132  FUN_ffff000090721014

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090721014(void)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 local_24;
  
  iVar2 = 0;
  local_24 = 0;
  do {
    uVar1 = FUN_ffff000090737554(_DAT_ffff000090860648,iVar2,&local_24);
    FUN_ffff00009077e11c
              (s__s_reg_0x_02x____0x_02x_ffff0000907e90a9,
               s_mt6379_2p_dump_registers_ffff0000907f7f12,iVar2,local_24);
    iVar2 = iVar2 + 1;
  } while (iVar2 != 0x21);
  return uVar1;
}


