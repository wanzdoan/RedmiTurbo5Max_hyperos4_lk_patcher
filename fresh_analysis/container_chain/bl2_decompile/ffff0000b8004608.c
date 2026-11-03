
uint FUN_ffff0000b8004608(void)

{
  byte bVar1;
  byte bVar2;
  uint uVar3;
  uint uVar4;
  byte local_24 [4];
  
  local_24[0] = 0;
  uVar3 = FUN_ffff0000b802127c(DAT_ffff0000b810c070,0x7f9,local_24,1);
  if (uVar3 == 0) {
    bVar2 = local_24[0] >> 6;
    bVar1 = 0xc0;
    if (bVar2 != 3) {
      bVar1 = (local_24[0] & 0xc0) + 0x40;
    }
    local_24[0] = bVar1 | local_24[0] & 0x3f;
    uVar4 = FUN_ffff0000b8021244(DAT_ffff0000b810c070,0x7f9,local_24,1);
    uVar3 = (uint)bVar2;
    if (uVar4 != 0) {
      FUN_ffff0000b804c33c
                (s__s__d__Failed_to_wrtie_SYSTEM_IN_ffff0000b8087d18,
                 s_get_boot_battery_times_ffff0000b8089866,0x1a0);
      uVar3 = uVar4;
    }
  }
  return uVar3;
}


