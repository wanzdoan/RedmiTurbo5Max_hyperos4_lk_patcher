// ffff0000907282cc  size=40  FUN_ffff0000907282cc

void FUN_ffff0000907282cc(long *param_1,ulong param_2,uint param_3)

{
  *(uint *)(*param_1 + (ulong)*(ushort *)(PTR_DAT_ffff000090839988 + (param_2 & 0xff) * 2)) =
       param_3 & 0xffff;
  return;
}


