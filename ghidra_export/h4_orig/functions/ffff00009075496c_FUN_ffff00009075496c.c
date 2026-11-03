// ffff00009075496c  size=476  FUN_ffff00009075496c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075496c(undefined4 param_1)

{
  uint *puVar1;
  uint uVar2;
  
  uVar2 = 4;
  puVar1 = (uint *)&DAT_ffff000032000108;
  switch(param_1) {
  case 0:
    break;
  case 1:
    uVar2 = 0x10000;
    break;
  default:
    goto code_r0xffff000090754b3c;
  case 4:
    uVar2 = 1;
    break;
  case 5:
    uVar2 = 0x100000;
    break;
  case 6:
    uVar2 = 0x20000;
    break;
  case 10:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 0x100;
    break;
  case 0xc:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 0x10;
    break;
  case 0xd:
    uVar2 = 0x1000000;
    break;
  case 0xe:
    uVar2 = 0x400000;
    break;
  case 0xf:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 0x200;
    break;
  case 0x12:
    uVar2 = 0x2000000;
    break;
  case 0x13:
    uVar2 = 0x800000;
    break;
  case 0x14:
    puVar1 = (uint *)&DAT_ffff000032000118;
    break;
  case 0x16:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 1;
    break;
  case 0x17:
    uVar2 = 0x4000000;
    break;
  case 0x18:
    uVar2 = 0x10000000;
    break;
  case 0x1a:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 8;
    break;
  case 0x1c:
    puVar1 = (uint *)&DAT_ffff000032000118;
    uVar2 = 2;
    break;
  case 0x1d:
    uVar2 = 0x8000000;
    break;
  case 0x1e:
    uVar2 = 0x20000000;
    break;
  case 0x2b:
    uVar2 = 0x2000;
    _DAT_ffff000032400108 = _DAT_ffff000032400108 | 0x400000;
    goto LAB_ffff000090754b08;
  case 0x2c:
    goto LAB_ffff000090754afc;
  case 0x2d:
    _DAT_ffff000032400108 = _DAT_ffff000032400108 | 0x400000;
    _DAT_ffff000032400118 = _DAT_ffff000032400118 | 0x2000;
LAB_ffff000090754afc:
    uVar2 = 0x4000;
    _DAT_ffff000032400108 = _DAT_ffff000032400108 | 0x800000;
LAB_ffff000090754b08:
    puVar1 = (uint *)&DAT_ffff000032400118;
    break;
  case 0x42:
    uVar2 = 0x40000;
    break;
  case 0x43:
    uVar2 = 0x80000;
  }
  *puVar1 = *puVar1 | uVar2;
code_r0xffff000090754b3c:
  return 0;
}


