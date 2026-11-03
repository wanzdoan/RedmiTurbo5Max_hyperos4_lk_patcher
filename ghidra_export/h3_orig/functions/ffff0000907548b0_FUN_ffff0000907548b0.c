// ffff0000907548b0  size=384  FUN_ffff0000907548b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907548b0(undefined4 param_1)

{
  uint *puVar1;
  uint uVar2;
  
  uVar2 = 4;
  puVar1 = (uint *)&DAT_ffff000032000104;
  switch(param_1) {
  case 0:
    break;
  case 1:
    uVar2 = 0x10000;
    break;
  default:
    goto code_r0xffff000090754a24;
  case 4:
    uVar2 = 1;
    break;
  case 10:
    puVar1 = (uint *)&DAT_ffff000032000114;
    uVar2 = 0x100;
    break;
  case 0xc:
    puVar1 = (uint *)&DAT_ffff000032000114;
    uVar2 = 0x10;
    break;
  case 0xd:
    uVar2 = 0x1000000;
    break;
  case 0xe:
    uVar2 = 0x400000;
    break;
  case 0x14:
    puVar1 = (uint *)&DAT_ffff000032000114;
    break;
  case 0x16:
    puVar1 = (uint *)&DAT_ffff000032000114;
    uVar2 = 1;
    break;
  case 0x17:
    uVar2 = 0x4000000;
    break;
  case 0x18:
    uVar2 = 0x10000000;
    break;
  case 0x2b:
    _DAT_ffff000032400114 = _DAT_ffff000032400114 | 0x2000;
    uVar2 = 0x400000;
    puVar1 = (uint *)&DAT_ffff000032400104;
    break;
  case 0x2c:
    _DAT_ffff000032400114 = _DAT_ffff000032400114 | 0x4000;
    uVar2 = 0x800000;
    puVar1 = (uint *)&DAT_ffff000032400104;
    break;
  case 0x2d:
    puVar1 = (uint *)&DAT_ffff000032400104;
    _DAT_ffff000032400104 = _DAT_ffff000032400104 | 0x400000;
    _DAT_ffff000032400114 = _DAT_ffff000032400114 | 0x6000;
    uVar2 = 0x800000;
    break;
  case 0x42:
    uVar2 = 0x40000;
    break;
  case 0x43:
    uVar2 = 0x80000;
  }
  *puVar1 = *puVar1 | uVar2;
code_r0xffff000090754a24:
  return 0;
}


