// ffff000090711380  size=252  FUN_ffff000090711380

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090711380(void)

{
  uint uVar1;
  uint uVar2;
  byte bVar3;
  int iVar4;
  
  bVar3 = FUN_ffff000090710b48();
  uVar1 = (uint)bVar3;
  uVar2 = uVar1;
  if (bVar3 != 0) {
    if (uVar1 == 8) {
      uVar2 = 2;
    }
    else {
      uVar2 = _DAT_ffff00009083f62c;
      if (uVar1 == 2) {
        uVar2 = 1;
      }
    }
  }
  _DAT_ffff00009083f62c = uVar2;
  FUN_ffff00009077eb88(0xffff00009083f630,0,0x100);
  iVar4 = FUN_ffff00009077f434
                    (0xffff00009083f630,0x100,s_block2mtd_block2mtd__dev_block_b_ffff0000907f3ec4,
                     0x200000,_DAT_ffff00009083f62c,0,0x200000,1,1,_DAT_ffff00009083f62c);
  if (iVar4 < 0x100) {
    FUN_ffff00009077de54(s_mtdoops_cmdline_is__s_ffff0000907f9225,0xffff00009083f630);
    iVar4 = FUN_ffff00009077ef88(0xffff00009083f630);
    if (iVar4 != 0) {
      FUN_ffff000090718f34(0xffff00009083f630);
      return;
    }
  }
  else {
    FUN_ffff00009077de54(s_Buffer_is_too_small_to_store_the_ffff0000908002d4);
  }
  FUN_ffff00009077de54(s_get_mtdoops_cmdline_fail__ffff0000907da538);
  return;
}


