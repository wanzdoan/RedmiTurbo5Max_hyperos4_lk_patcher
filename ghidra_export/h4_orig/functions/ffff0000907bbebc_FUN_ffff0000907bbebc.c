// ffff0000907bbebc  size=376  FUN_ffff0000907bbebc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907bbebc(long param_1,int *param_2)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar2 = FUN_ffff00009077f6fc
                    (param_1 + *param_2,0xbff - (long)*param_2,
                     s_tracker_status__0x_lx___s_ffff0000907f0abc,_DAT_ffff00001c3a4000,
                     &DAT_ffff000090813740 +
                     *(int *)(&DAT_ffff000090813740 + ((ulong)(_DAT_ffff00001c3a4000 >> 8) & 3) * 4)
                    );
  uVar3 = 0;
  puVar4 = (uint *)&DAT_ffff00001c3a4200;
  iVar2 = *param_2 + iVar2;
  *param_2 = iVar2;
  do {
    uVar1 = *puVar4;
    iVar2 = FUN_ffff00009077f6fc
                      (param_1 + iVar2,(long)(0xbff - iVar2),
                       s_read_entry____u__timeout___0x_lx_ffff0000907d3ba9,uVar3 & 0xffffffff,
                       uVar1 >> 0xe & 1,uVar1 >> 0xd & 1,uVar1 >> 0xc & 1,uVar1 >> 8 & 0xf,
                       uVar1 >> 4 & 7,uVar1 & 0xf,puVar4[0x80]);
    uVar3 = uVar3 + 1;
    puVar4 = puVar4 + 1;
    iVar2 = *param_2 + iVar2;
    *param_2 = iVar2;
  } while (uVar3 != 8);
  uVar3 = 0;
  puVar4 = (uint *)&DAT_ffff00001c3a4800;
  do {
    uVar1 = *puVar4;
    iVar2 = FUN_ffff00009077f6fc
                      (param_1 + iVar2,(long)(0xbff - iVar2),
                       s_write_entry____u__timeout___0x_l_ffff0000907f230e,uVar3 & 0xffffffff,
                       uVar1 >> 0xe & 1,uVar1 >> 0xd & 1,uVar1 >> 0xc & 1,uVar1 >> 8 & 0xf,
                       uVar1 >> 4 & 7,uVar1 & 0xf,puVar4[0x80]);
    uVar3 = uVar3 + 1;
    puVar4 = puVar4 + 1;
    iVar2 = *param_2 + iVar2;
    *param_2 = iVar2;
  } while (uVar3 != 8);
  return -(uint)(0xc00 < iVar2);
}


