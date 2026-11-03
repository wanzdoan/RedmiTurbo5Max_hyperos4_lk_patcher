
/* WARNING: Removing unreachable block (ram,0xffff00009074a8f8) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a904) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a910) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a914) */
/* WARNING: Removing unreachable block (ram,0xffff00009074aa70) */
/* WARNING: Removing unreachable block (ram,0xffff00009074ab28) */
/* WARNING: Removing unreachable block (ram,0xffff00009074ab38) */
/* WARNING: Removing unreachable block (ram,0xffff00009074aa90) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a940) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a95c) */
/* WARNING: Removing unreachable block (ram,0xffff00009074aaa4) */
/* WARNING: Removing unreachable block (ram,0xffff00009074a994) */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009074a7dc(uint param_1,uint param_2)

{
  long lVar1;
  undefined8 *puVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  ulong uVar6;
  
  uVar6 = (ulong)param_1;
  if ((DAT_ffff000090877710 & 1) == 0) {
    iVar4 = FUN_ffff000090784ca8();
    if (iVar4 != 0) {
      DAT_ffff00009081aca0 = 0xffff000013260f3c;
      DAT_ffff00009081acc0 = 0xffff000013260f40;
      DAT_ffff00009081ace0 = 0xffff000013260f44;
      DAT_ffff00009081ad00 = 0xffff000013260f48;
      DAT_ffff00009081ad20 = 0xffff000013260f4c;
      DAT_ffff00009081ad40 = 0xffff000013260f50;
      DAT_ffff00009081ad60 = 0xffff000013260f54;
      DAT_ffff00009081ad80 = 0xffff000013260f58;
      DAT_ffff00009081adc0 = 0xffff000013264110;
      DAT_ffff00009081ade0 = 0xffff000013264114;
      DAT_ffff00009081ada0 = 0xffff00001326410c;
      DAT_ffff00009081ae00 = 0xffff000013264118;
      DAT_ffff00009081ae20 = 0xffff00001326411c;
      DAT_ffff00009081ae40 = 0xffff000013264120;
      DAT_ffff00009081ae60 = 0xffff000013260cc8;
      DAT_ffff00009081ae80 = 0xffff000013260ccc;
      DAT_ffff00009081aea0 = 0xffff000013260ba8;
    }
    DAT_ffff000090877710 = 1;
  }
  if (7 < param_1) {
    return 0x1000;
  }
  lVar1 = uVar6 * 0x20;
  puVar2 = (undefined8 *)(&DAT_ffff00009081ac90 + lVar1);
  _DAT_ffff00001326f050 = _DAT_ffff00001326f050 & 0xfffffff0;
  uVar5 = *(uint *)*puVar2;
  uVar3 = *(uint *)(&DAT_ffff00009081ac98 + lVar1) & (uVar5 ^ 0xffffffff) & param_2;
  FUN_ffff00009077e11c(s__Run_Time__Original___0x_x__Blow_ffff0000907ff58a,uVar5,param_2,uVar3);
  if (uVar3 == 0) {
    FUN_ffff00009077e11c(s__Run_Time__Already_blown__Bypass_ffff0000907f6983);
    return 0;
  }
  if ((((&DAT_ffff00009081ac9d)[lVar1] & 0xfe) == 2) &&
     (((*(uint *)(&DAT_ffff00009081ac98 + lVar1) & uVar5) != 0 ||
      ((*(uint *)(&DAT_ffff00009081aca8 + uVar6 * 0x20) &
       *(uint *)(&DAT_ffff00009081aca0)[uVar6 * 4]) != 0)))) {
    FUN_ffff00009077e11c(s__Run_Time__This_field_can_only_b_ffff0000907e6653);
    uVar5 = 0x4000;
    goto LAB_ffff00009074aae4;
  }
  thunk_FUN_ffff000090746154();
  iVar4 = FUN_ffff00009074a4cc();
  if (iVar4 == 0) {
    uVar5 = FUN_ffff00009074ab74();
    if (uVar5 != 0) goto LAB_ffff00009074aae4;
    uVar5 = FUN_ffff00009074abc8(*puVar2,uVar3);
    if (uVar5 != 0) {
      uVar5 = uVar5 | 0x100000;
      goto LAB_ffff00009074aae4;
    }
    FUN_ffff00009074ab74(1);
    iVar4 = FUN_ffff00009074a59c();
    if (iVar4 == 0) {
      FUN_ffff00009074ac28();
      FUN_ffff00009077e11c
                (s__Run_Time__Read_check_after_blow_ffff0000907f3015,*(undefined4 *)*puVar2);
      return 0;
    }
  }
  uVar5 = 0x20000;
LAB_ffff00009074aae4:
  FUN_ffff00009077e11c(s__Run_Time__Blow_Fail__Status___0_ffff0000907d1627,uVar5);
  iVar4 = FUN_ffff00009074a610();
  if (iVar4 != 0) {
    FUN_ffff00009074a59c();
  }
  FUN_ffff00009074ab74(1);
  return uVar5;
}


