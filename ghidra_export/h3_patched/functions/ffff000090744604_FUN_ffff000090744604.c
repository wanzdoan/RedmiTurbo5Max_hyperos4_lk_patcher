// ffff000090744604  size=672  FUN_ffff000090744604

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090744604(undefined4 *param_1)

{
  uint uVar1;
  undefined4 *puVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  int iVar7;
  
  iVar7 = _DAT_ffff0000908774d0 - _DAT_ffff0000908774c8;
  FUN_ffff00009077de54
            (s__s__d___MMUP__tbuf_size___0x_x_ffff0000907eae97,s_mmup_do_tbufdump_ffff0000907dc9cc,
             0xa1,iVar7);
  if (iVar7 - 0x4001U < 0xffffc000) {
    FUN_ffff00009077de54
              (s__MMUP__error___s_tbuf_size_0x_x__ffff0000907e3018,
               s_mmup_do_tbufdump_ffff0000907dc9cc,iVar7,0x4000);
    iVar7 = 0;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,iVar7);
    lVar5 = 0;
    iVar4 = _DAT_ffff000031a0d08c * 8;
    uVar3 = _DAT_ffff000031a0d010 & 0xffffff8f;
    do {
      uVar1 = iVar4 + (int)lVar5;
      uVar6 = (ulong)(uVar1 & 8);
      _DAT_ffff000031a0d010 = uVar1 & 0xfffffff0 | uVar3;
      puVar2 = (undefined4 *)((long)param_1 + lVar5);
      lVar5 = lVar5 + 8;
      *puVar2 = *(undefined4 *)(uVar6 | 0xffff000031a0d0e0);
      puVar2[1] = *(undefined4 *)(uVar6 | 0xffff000031a0d0e4);
    } while ((int)lVar5 != 0x80);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0,*param_1,param_1[1]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,1,param_1[2],param_1[3]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,2,param_1[4],param_1[5]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,3,param_1[6],param_1[7]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,4,param_1[8],param_1[9]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,5,param_1[10],param_1[0xb]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,6,param_1[0xc],param_1[0xd]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,7,param_1[0xe],param_1[0xf]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,8,param_1[0x10],param_1[0x11]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,9,param_1[0x12],param_1[0x13]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,10,param_1[0x14],param_1[0x15]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0xb,param_1[0x16],param_1[0x17]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0xc,param_1[0x18],param_1[0x19]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0xd,param_1[0x1a],param_1[0x1b]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0xe,param_1[0x1c],param_1[0x1d]);
    FUN_ffff00009077de54
              (s__s__d___MMUP__C1__02d_0x_08x__0x_ffff0000907d7be9,
               s_mmup_do_tbufdump_ffff0000907dc9cc,0xb7,0xf,param_1[0x1e],param_1[0x1f]);
  }
  return iVar7;
}


