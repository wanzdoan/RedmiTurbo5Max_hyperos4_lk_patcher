
undefined1 FUN_ffff0000b803ffb8(void)

{
  undefined1 uVar1;
  byte bVar2;
  int iVar3;
  
  bVar2 = thunk_FUN_ffff0000b802ce0c(0x1e);
  iVar3 = FUN_ffff0000b80504b0();
  uVar1 = 2;
  if (bVar2 != 0x13 && (bVar2 & 0xf7) != 5) {
    uVar1 = iVar3 == 1;
  }
  FUN_ffff0000b804c33c
            (s__s__cpu_lv____d__sw_ver____d__se_ffff0000b809ecc0,s_get_cpu_level_ffff0000b808af67,
             uVar1,iVar3,bVar2);
  return uVar1;
}


