// ffff000090707fd4  size=572  FUN_ffff000090707fd4

void FUN_ffff000090707fd4(long param_1)

{
  char *pcVar1;
  ulong uVar2;
  undefined8 uVar3;
  uint uVar4;
  undefined4 uVar5;
  int iVar6;
  undefined4 uVar7;
  ulong uVar8;
  long *plVar9;
  ulong uVar10;
  
  uVar2 = esr_el1;
  uVar8 = uVar2 >> 0x1a & 0x3f;
  uVar4 = (uint)uVar2;
  iVar6 = (int)uVar8;
  switch(iVar6) {
  case 7:
    FUN_ffff0000907085f8();
    return;
  default:
    FUN_ffff00009077e11c(s_unhandled_synchronous_exception_ffff0000907f79cd);
    break;
  case 0xd:
    FUN_ffff00009077e11c
              (s_branch_target_exception__BTYPE_0_ffff0000907f9284,uVar2 & 3,
               *(undefined8 *)(param_1 + 0x100));
    break;
  case 0x11:
  case 0x15:
    FUN_ffff000090707fbc(param_1,iVar6 == 0x15);
    return;
  case 0x1c:
    uVar7 = 0x49;
    if ((uVar2 & 2) != 0) {
      uVar7 = 0x44;
    }
    uVar5 = 0x41;
    if ((uVar2 & 1) != 0) {
      uVar5 = 0x42;
    }
    FUN_ffff00009077e11c
              (s_PAC_auth_failure_target_exceptio_ffff000090800337,uVar7,uVar5,
               *(undefined8 *)(param_1 + 0x100));
    break;
  case 0x20:
  case 0x21:
    FUN_ffff00009077e11c
              (s_instruction_abort__PC_at_0x_llx_ffff0000907d72fc,*(undefined8 *)(param_1 + 0x100));
    uVar10 = (ulong)(uVar4 & 0x3f);
    goto LAB_ffff0000907080b8;
  case 0x24:
  case 0x25:
    if (PTR_DAT_ffff000090839860 < PTR_DAT_ffff000090839858) {
      plVar9 = (long *)PTR_DAT_ffff000090839860;
      do {
        if (*plVar9 == *(long *)(param_1 + 0x100)) {
          *(long *)(param_1 + 0x100) = plVar9[1];
          return;
        }
        plVar9 = plVar9 + 2;
      } while (plVar9 < PTR_DAT_ffff000090839858);
    }
    uVar10 = uVar2 & 0x3f;
    uVar3 = far_el1;
    pcVar1 = &DAT_ffff0000907e249b;
    if ((uVar2 & 0x40) != 0) {
      pcVar1 = s_Write_ffff0000907ea5d4;
    }
    FUN_ffff00009077e11c
              (s_data_fault___s_access_from_PC_0x_ffff0000907e40e6,pcVar1,
               *(undefined8 *)(param_1 + 0x100),uVar3,uVar4 & 0x1ffffff,uVar10);
LAB_ffff0000907080b8:
    FUN_ffff000090708210(uVar10);
    break;
  case 0x3c:
    FUN_ffff00009077e11c
              (s_BRK__0x_04lx_instruction__PC_at_0_ffff0000907e412f,uVar2 & 0xffff,
               *(undefined8 *)(param_1 + 0x100));
  }
  FUN_ffff00009077e11c
            (s_ESR_0x_x__ec_0x_x__il_0x_x__iss_0_ffff0000907e73b5,uVar2,uVar8,uVar4 & 0x2000000,
             uVar4 & 0x1ffffff);
  FUN_ffff000090708378(param_1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(&DAT_ffff0000907e8c5f);
}


