// ffff000090705198  size=140  FUN_ffff000090705198

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090705198(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 local_24;
  
  local_24 = 0;
  iVar1 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x70,&local_24);
  if (iVar1 == 0) {
    if ((char)local_24 < '\0') {
      uVar2 = 0x10;
    }
    else {
      uVar2 = 1;
    }
    iVar1 = FUN_ffff0000907057d4(uVar2,param_1);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_regmap_read_MT6_ffff000090801b85,
               s__mt6379_charger_get_vbus_ffff0000907e71ad,0x269);
  }
  return iVar1;
}


