// File offset entry: 0x66c24

void FUN_02066b24(uint param_1)

{
  uRam000000000211be35 = 1;
  func_0x02086088(0xa0d,param_1 & 0xf,0xf,0);
  uRam000000000211be35 = 0;
  uRam000000000011df78 = uRam000000000011df78 & 0xfffffff0 | param_1 & 0xf;
  return;
}


