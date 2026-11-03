// ffff000090739c38  size=68  FUN_ffff000090739c38

undefined8 FUN_ffff000090739c38(long param_1,uint param_2)

{
  if (0x1fffe < param_2) {
    param_2 = 0x1ffff;
  }
  FUN_ffff000090739868
            ((uint *)(param_1 + 0x50),param_2 | 0x95000000 | *(uint *)(param_1 + 0x50) & 0xfffe0000)
  ;
  return 0;
}


